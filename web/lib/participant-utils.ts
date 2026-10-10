import { components } from "@/types/api";

export function getParticipantDisplayName(
  participant: components["schemas"]["ParticipantResponse"]
) {
  return `${participant.username} (${participant.firstName} ${participant.lastName})`;
}
