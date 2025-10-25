package edu.utn.frsf.isi.dan.gestion.controller;

import edu.utn.frsf.isi.dan.gestion.model.AmenityHotel;
import edu.utn.frsf.isi.dan.gestion.model.Hotel;
import edu.utn.frsf.isi.dan.gestion.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@RestController
@RequestMapping("/hoteles")
public class HotelController {
    @Autowired
    private HotelService hotelService;

    @PostMapping
    public ResponseEntity<Hotel> create(@RequestBody Hotel hotel) {
        Hotel newHotel = hotelService.save(hotel);
        if(newHotel == null) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(newHotel);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Hotel> getById(@PathVariable Integer id) {
        return hotelService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<Hotel> getAll() {
        return hotelService.findAll();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Hotel> update(@PathVariable Integer id, @RequestBody Hotel hotel) {
        if (!hotelService.findById(id).isPresent()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(hotelService.update(id, hotel.getCorreoContacto(), hotel.getTelefono(), hotel.getCategoria()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (!hotelService.findById(id).isPresent()) return ResponseEntity.notFound().build();
        hotelService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/amenities")
    public ResponseEntity<Hotel> addAmenities(@PathVariable Integer id, @RequestBody List<AmenityHotel> newAmenities) {
        if (!hotelService.findById(id).isPresent()) return ResponseEntity.notFound().build();
        Hotel hotel = hotelService.addAmenities(id, newAmenities);
        if(hotel == null) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(hotel);
    }

    @DeleteMapping("/{id}/amenities/{amenityId}")
    public ResponseEntity<Void> deleteAmenity(@PathVariable Integer id,@PathVariable Long amenityId) {
        if (!hotelService.findById(id).isPresent()) return ResponseEntity.notFound().build();
        if (!hotelService.findById(id).get().getAmenities().stream().anyMatch(amenity -> amenity.getId() == amenityId)) return ResponseEntity.notFound().build();
        hotelService.deleteAmenityHotel(id, amenityId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/findBy")
    public ResponseEntity<List<Hotel>> findHotelsByCriteria(@RequestParam(required = false) String nombre,
            @RequestParam(required = false) Integer categoria,
            @RequestParam(required = false) List<String> amenities) {
        List<Hotel> hotels = hotelService.findHotelsByCriteria(nombre, categoria, amenities);
        if(hotels != null) return ResponseEntity.ok(hotels);
        return ResponseEntity.notFound().build();
    }
    
}
