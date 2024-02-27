package ies.goya.examen;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("personajes")
public class PersonajeController {
	
	@Autowired
	RepositoryPersonaje repositorio;
	
	private ArrayList<String> log = new ArrayList<String>();
	
	@GetMapping()
	public ResponseEntity<Iterable<Personaje>> getAll() {
		log.add(LocalDateTime.now()  + ": Petición GET / recibida"); // Dejar tal cual
		return ResponseEntity.ok(repositorio.findAll()); // CAMBIAR
	}
	
	@GetMapping("{id}")
	public ResponseEntity<?> getOne(@PathVariable(name="id") Long id) {
		log.add(LocalDateTime.now() + ": Petición GET recibida con id=" + id); // Dejar tal cual
		Optional<Personaje> existePersonaje = repositorio.findById(id);
		if (!existePersonaje.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El elemento no existe");
		}
		return ResponseEntity.ok(repositorio.findById(id)); // CAMBIAR
	}
	
	/*
	 * ... implementar el resto de mappings
	 */
	
	@PostMapping
	public ResponseEntity<?> createOne(@RequestBody Personaje nuevoPersonaje){
		Optional<Personaje> existePersonaje = repositorio.findByNombre(nuevoPersonaje.getNombre());
		if (existePersonaje.isPresent()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El elemento ya existe");
		} else {
			if (nuevoPersonaje.getId() != null) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No puedes indicar id en el cuerpo.");
			} else {
				repositorio.save(nuevoPersonaje);
				return ResponseEntity.status(HttpStatus.CREATED).body(nuevoPersonaje);
			}

		}	
	}//post
	
	@PutMapping("{id}")
	public ResponseEntity<?> reemplazoById(
			@PathVariable(name="id") Long id,
			@RequestBody Personaje actualizadoPersonaje){
		Optional<Personaje> existePersonaje = repositorio.findById(id);
		if (actualizadoPersonaje.getId() != id) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("ID diferente.");
		}
		if (!existePersonaje.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El elemento no existe");
		} else {
			repositorio.save(actualizadoPersonaje);
			return ResponseEntity.status(HttpStatus.OK).body(actualizadoPersonaje);
		}
	}//PUT
	
	@PatchMapping("{id}")
	public ResponseEntity<?> actualizarById(
			@PathVariable(name="id") Long id,
			@RequestBody Personaje actualizadoPersonaje){
		Optional<Personaje> existePersonaje = repositorio.findById(id);
		Personaje existePerson = repositorio.findById(id).orElse(null);
		if (!existePersonaje.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El elemento no existe");
		} else {
			if(actualizadoPersonaje.getDescripción() != null) {
				existePerson.setDescripción(actualizadoPersonaje.getDescripción());
			}
			if (actualizadoPersonaje.getNombre() != null) {
				existePerson.setNombre(actualizadoPersonaje.getNombre());
			}
			return ResponseEntity.status(HttpStatus.OK).body(actualizadoPersonaje);
		}
		
	}//patch
	
	@DeleteMapping("{id}")
	public ResponseEntity<?> deleteById(@PathVariable(name="id") Long id){
		Optional<Personaje> existePersonaje = repositorio.findById(id);
		if (!existePersonaje.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El elemento no existe");
		} else {
			repositorio.deleteById(id);
			return ResponseEntity.status(HttpStatus.OK).body(existePersonaje);
		}
	}
	
	
	
	// Ignorar este método de control
	@GetMapping("/log")
	public String getLog() {
		log.add(LocalDateTime.now()  + ": Petición /log"); 
		return log.toString().replaceAll("[,\\]\\[]","<br>\n");
	}
}
