# Backend Banco Cloud – Gestión de Cheques con RabbitMQ

Monorepo con 3 microservicios Spring Boot independientes (Java 17, Maven multi-módulo).

| Módulo | Puerto | Rol |
|---|---|---|
| `ms-cheques` | 8081 | API REST de cheques + **productor** de eventos. MySQL + JWT |
| `ms-notificaciones` | 8082 | **Consumidor** (`@RabbitListener`, ACK manual, DLQ). Sin REST |
| `ms-rabbit-admin` | 8083 | API REST para crear/eliminar colas, exchanges y bindings. JWT (ADMIN) |

## Flujo de mensajería

```
ms-cheques --(banco.cheques.exchange / routing key)--> cheque.*.queue --> ms-notificaciones
                                                          | nack(requeue=false)
                                                          v
                                  banco.cheques.dlx --> cheque.*.dlq --> log "MENSAJE EN DLQ"
```

## Cómo ejecutar

1. Levantar RabbitMQ: `docker compose up -d` (panel: http://localhost:15672, guest/guest)
2. Variables de entorno (NO subir credenciales al repo): `DB_URL`, `DB_USER`, `DB_PASSWORD`,
   `JWT_ISSUER_URI`, `JWT_ROLES_CLAIM` (claim del JWT con los roles; deben llamarse `USER` / `ADMIN`).
3. **Arrancar primero `ms-notificaciones`** (declara colas, DLQ y bindings) y luego los otros:
   ```
   mvn -pl ms-notificaciones spring-boot:run
   mvn -pl ms-cheques spring-boot:run
   mvn -pl ms-rabbit-admin spring-boot:run
   ```
4. Compilar y probar todo: `mvn clean verify`

## Endpoints

**ms-cheques** (`Authorization: Bearer <JWT>`)
- `POST /api/cheques` (USER) `{ "beneficiario": "...", "monto": 1000, "fecha": "2026-11-30" }`
- `GET /api/cheques/mios` (USER)
- `GET /api/cheques` (ADMIN)
- `PATCH /api/cheques/{id}/estado` (ADMIN) `{ "estado": "COBRADO" }`
- `POST /api/cheques/{id}/devolucion` (ADMIN) `{ "motivo": "Fondos insuficientes" }`

**ms-rabbit-admin** (ADMIN)
- `POST /api/rabbit/queues` · `GET /api/rabbit/queues/{name}` · `DELETE /api/rabbit/queues/{name}`
- `POST /api/rabbit/exchanges` · `DELETE /api/rabbit/exchanges/{name}`
- `POST /api/rabbit/bindings` · `DELETE /api/rabbit/bindings?queue=&exchange=&routingKey=`

## Cómo demostrar la DLQ

En el panel de RabbitMQ (Queues → `cheque.emitido.queue` → Publish message) publica un JSON con
`"monto": -5`. El consumidor lo rechaza como no recuperable, el mensaje pasa a `cheque.emitido.dlq`
y `DeadLetterConsumer` lo registra en el log.
