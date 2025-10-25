package edu.utn.frsf.isi.dan.gestion.controller;

import edu.utn.frsf.isi.dan.gestion.model.Tarifa;
import edu.utn.frsf.isi.dan.gestion.model.TipoHabitacion;
import edu.utn.frsf.isi.dan.gestion.service.TarifaService;
import edu.utn.frsf.isi.dan.gestion.service.TipoHabitacionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.foreign.Linker.Option;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tarifas")
public class TarifaController {
    @Autowired
    private TarifaService tarifaService;

    @Autowired
    private TipoHabitacionService    tipoHabitacionService;

    @PostMapping
    public ResponseEntity<Tarifa> create(@RequestBody Tarifa tarifa) {
        return ResponseEntity.ok(tarifaService.save(tarifa));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tarifa> getById(@PathVariable Integer id) {
        return tarifaService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<Tarifa> getAll() {
        return tarifaService.findAll();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tarifa> update(@PathVariable Integer id, @RequestBody Tarifa tarifa) {
        if (!tarifaService.findById(id).isPresent()) return ResponseEntity.notFound().build();
        tarifa.setId(id);
        return ResponseEntity.ok(tarifaService.save(tarifa));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        Optional<Tarifa> tarifa = tarifaService.findById(id);
        if (!tarifa.isPresent()) return ResponseEntity.notFound().build();
        tarifaService.delete(tarifa.get());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/habitacion/{id}")
    public ResponseEntity<Optional<Tarifa>> getByHabitacionId(@PathVariable Integer id) {
        Optional<Tarifa> tarifa = tarifaService.findByHabitacionId(id);
        if (tarifa.isPresent()) {
            return ResponseEntity.ok(tarifa);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}")
    public ResponseEntity<Tarifa> create(@PathVariable Integer id, @RequestBody Tarifa tarifa) {
        Optional<TipoHabitacion> tipoHabitacion = tipoHabitacionService.findById(id);
        if (!tipoHabitacion.isPresent()) return ResponseEntity.notFound().build();
        Tarifa tarifaSaved = tarifaService.createNewTarifa(tarifa, tipoHabitacion.get());
        if (tarifaSaved == null) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(tarifaSaved);
    }

    
}

