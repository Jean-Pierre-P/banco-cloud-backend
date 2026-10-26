package cl.duoc.bancocloud.rabbitadmin.dto;

public record QueueInfoResponse(String name, int messageCount, int consumerCount) {
}
