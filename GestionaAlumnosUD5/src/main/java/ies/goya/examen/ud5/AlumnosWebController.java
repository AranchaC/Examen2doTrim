package ies.goya.examen.ud5;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AlumnosWebController {
//	protected Map<Long,Alumno> repositorioAlumnos = new LinkedHashMap<Long,Alumno>(); // A sustituir por BBDD
//	protected long contadorIds = 1; // a borrar cuando lo haga la BBDD
	
	@Autowired
	AlumnoRepositorio repositorioAlumnos;
	
	@GetMapping("/todos")
	public List<Alumno> todos() {
		return new ArrayList<Alumno>(repositorioAlumnos.findAll());
	}
	
	@GetMapping("/alumnoPorId")
	public Optional<Alumno> alumnoPorId(@RequestParam(name="id") Long id) {
		return repositorioAlumnos.findById(id);
	}
	
	// En una app real los datos vendrías por POST
	@GetMapping("/nuevoAlumno")
	public Alumno nuevoAlumno(@RequestParam(name="nombre") String nombre,
			@RequestParam(name="apellidos") String apellidos) {
		Alumno alumno = new Alumno();
		//alumno.setId(contadorIds++);
		alumno.setNombre(nombre);
		alumno.setApellidos(apellidos);
		repositorioAlumnos.save(alumno);
		//repositorioAlumnos.put(alumno.getId(), alumno);
		return alumno;
	}
	/*
	@GetMapping("/modificaAlumno")
	public Alumno modificaAlumno(@RequestParam(name="id") Long id,
			@RequestParam(name="nombre", required=false) String nombre,
			@RequestParam(name="apellidos", required=false) String apellidos) {
		Optional<Alumno> alumno = repositorioAlumnos.findById(id);
		if (alumno == null) return null;
		if (nombre != null) alumno.setNombre(nombre);
		if (apellidos != null) alumno.setApellidos(apellidos);
		repositorioAlumnos.put(id, alumno);
		return alumno;
	}
	*/
	@GetMapping("/borraAlumno")
	public Alumno borraAlumno(@RequestParam(name="id") Long id) {
		Alumno alumno = repositorioAlumnos.findById(id).orElse(null);
		repositorioAlumnos.delete(alumno);
		return alumno;
	}
	
	@GetMapping("/meteEmailAlumno")
	public Alumno meteEmailAlumno(@RequestParam(name="id") Long id,
			@RequestParam(name="email") String email) {
		Alumno alumno = repositorioAlumnos.findById(id).orElse(null);
		if (alumno != null) alumno.addEmail(new Email(email));
		repositorioAlumnos.save(alumno);
		return alumno;
	}
	
	@GetMapping("/borraEmailDeAlumno")
	public Alumno borraEmailDeAlumno(@RequestParam(name="id") Long id,
			@RequestParam(name="email") String email) {
		Alumno alumno = repositorioAlumnos.findById(id).orElse(null);
		if (alumno != null) alumno.delEmail(new Email(email));
		return alumno;
		
	}
	
	// findByNameAndLocation(String name, String location)
	@GetMapping("/buscaPorNombreYApellidos") 
	public Alumno buscaAlumno(@RequestParam(name="nombre") String nombre,
							@RequestParam(name="apellidos") String apellidos) {
		for (Alumno alum : repositorioAlumnos.findAll())
			if (alum.getNombre().equals(nombre) && (alum.getApellidos().equals(apellidos)))
				return alum;
		return null;
	}
	
	@GetMapping("/")
	public String indice() {
		return "Bienvenido a GesAlumnos:"
				+ "Use <a href=/todos>/todos</a> para consultar todos"
				+ "<br> <a href=/alumnoPorId>/alumnosPorId</a> para buscar por id"
				+ "<br> <a href=/nuevoAlumno>/nuevoAlumno</a> para meter un nuevo alumno"
				+ "<br> <a href=/modificaAlumno>/modificaAlumno</a> para modificar"
				+ "<br> <a href=/borraAlumno>/borraAlumno</a> para borrar (por id)"
				+ "<br> <a href=/borraEmailDeAlumno>/borraEmailDeAlumno</a> para borrar email de un alumno por id"
				+ "<br> <a href=/meteEmailAlumno>/meteEmailAlumno</a> para añadir email a un alumno (por id)"
				+ "<br> <a href=/buscaPorNombreYApellidos>/buscaPorNombreYApellidos</a> para buscar por nombre y apellidos";
	}
	
} // class
