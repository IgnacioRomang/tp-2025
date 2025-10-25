package edu.utn.frsf.isi.dan.gestion.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Amenity {
    
    // 1. Mapear constantes existentes a los valores de la DB
    PILETA("Pileta"), 
    SAUNA("Sauna"), 
    GIMNASIO("Gimnasio"), 
    RESTAURANTE("Restaurante"),
    BAR("Bar 24hs"), // Mapeado a "Bar 24hs"
    ESTACIONAMIENTO("Estacionamiento"),
    WIFI("Wi-Fi de alta velocidad"), // Mapeado a "Wi-Fi de alta velocidad"
    AIRE_ACONDICIONADO("Aire Acondicionado"), 
    CALENTADOR("Calentador"), 
    TV_CABLE("TV Cable"),
    SERVICIO_HABITACIONES("Servicio Habitaciones"), 
    LIMPIEZA_DIARIA("Limpieza Diaria"), 
    PISCINA_CUBIERTA("Piscina Cubierta"), 
    PISCINA_DESCUBIERTA("Piscina Descubierta"), 
    SPA("Spa y Sauna"), // Mapeado a "Spa y Sauna"
    SALA_JUEGOS("Sala de Juegos"), 
    SALA_REUNIONES("Sala de Reuniones"),
    TRANSPORTE_AEROPUERTO("Transporte Aeropuerto"), 
    PISCINA_CLIMATIZADA("Piscina Climatizada"), // Mapeado a "Piscina Climatizada"
    GIMNASIO_24HS("Gimnasio 24hs"), // Mapeado a "Gimnasio 24hs"
    
    // 2. Añadir las nuevas constantes que existen en la DB pero no en el Enum original
    ESTACIONAMIENTO_VALET("Estacionamiento Valet Parking"),
    DESAYUNO_BUFFET("Desayuno Buffet");


    private final String displayValue;

    Amenity(String displayValue) {
        this.displayValue = displayValue;
    }

    // Indica a Jackson que use este valor al SERIALIZAR (de Java a JSON)
    @JsonValue 
    public String getDisplayValue() {
        return displayValue;
    }

    // Indica a Jackson cómo DESERIALIZAR (de DB/JSON a Java Enum)
    @JsonCreator
    public static Amenity fromValue(String value) {
        for (Amenity amenity : Amenity.values()) {
            // Se usa equalsIgnoreCase para mayor robustez
            if (amenity.displayValue.equalsIgnoreCase(value)) {
                return amenity;
            }
        }
        // Lanza la excepción si el valor de la DB no se encuentra
        throw new IllegalArgumentException("Valor de Amenity no mapeado: " + value);
    }
}