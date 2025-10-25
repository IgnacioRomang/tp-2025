package edu.utn.frsf.isi.dan.shared;
import lombok.*;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HotelEvent {
    private HotelDTO hotel;
    private TipoEvento tipoEvento;
}
