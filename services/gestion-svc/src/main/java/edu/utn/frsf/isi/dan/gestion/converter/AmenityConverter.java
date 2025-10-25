package edu.utn.frsf.isi.dan.gestion.converter; // O la ubicación que uses para Converters

import edu.utn.frsf.isi.dan.gestion.enums.Amenity;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.stream.Stream;

@Converter(autoApply = true)
public class AmenityConverter implements AttributeConverter<Amenity, String> {

    // 1. Convertir de Java (Enum) a la Base de Datos (String)
    @Override
    public String convertToDatabaseColumn(Amenity amenity) {
        if (amenity == null) {
            return null;
        }
        // Usamos el displayValue que definiste en el Enum
        return amenity.getDisplayValue(); 
    }

    // 2. Convertir de la Base de Datos (String) a Java (Enum)
    @Override
    public Amenity convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        
        // Buscamos la constante Amenity que coincida con el valor de la base de datos
        // Utilizamos el método estático fromValue que creaste en tu Enum (que usa displayValue)
        try {
            return Amenity.fromValue(dbData); 
        } catch (IllegalArgumentException e) {
            // Si el valor no es válido, lanzará la excepción, confirmando que es la causa.
            // Es la misma excepción que veías: "No enum constant..."
            throw new IllegalArgumentException("El valor '" + dbData + 
                                               "' de la base de datos no es una Amenity válida.", e);
        }
    }
}