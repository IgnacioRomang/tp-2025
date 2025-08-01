# TP - ETAPA 01
Registrar usuarios. Los usuarios son de dos tipos _Huesped_ y _Propietario_ Para todo tipo de usuario se registrara el nombre, el email y el telefono. 

[Diagrama de modelo de datos propuesto](services/user-svc/modelo.mmd)

## Registrar Huespedes
De cada huesped se registrará además de los datos de usuario la información de una tarjeta de crédito al registrarse. De la tarjeta se registra
  - el numero
  - el nombre del titular
  - el codigo de seguridad
  - la fecha de vencimiento
  - el banco emisor
  - un valor booleano indicando si es la tarjeta principal (el usuario puede tener mas de una tarjeta pero solo una principal)

## Registrar Propietarios
De cada propietario se registrará además de los datos de usuario 
  - la cuenta bancaria 
  - el id de hotel (en la primer etapa este valor sera null porque se creará en el microservicio de la etapa 2)

## Gestionar usuarios
- De los usuarios huesped se pueden actualizar los datos del usuario y la fecha de nacimiento
- Se deben poder buscar usuarios por nombre o DNI (agregue este campo al modelo y a la base de datos)
- Borrar un huesped del sistema. Borrar en cascada también sus tarjetas de crédito.
- no se pueden borrar propietarios

## Gestionar tarjetas de credito
- Se debe permitir agregar una tarjeta de crédito a un usuario huesped. Si se la agrega como principal entonces hay que desmarcar como principal la tarjeta de crédito anterior.
- Eliminar una tarjeta de credito de la lista si no es la principal, caso contrario retornar error.
- Cambiar la tarjeta de crédito principal.
 
## Gestionar bancos
- Se deben poder realizar todas las operaciones de CRUD para los bancos.

## Consideraciones generales:
- Puede usar repositorios JPA o JDBC
- Debe exponer los endpoints RES
- Puede usar swagger para probar los endpoints
