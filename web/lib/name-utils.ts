import { components } from "@/types/api";

export function getDisplayNameFromParticipant(
  participant: components["schemas"]["ParticipantResponse"]
) {
  return getDisplayName(
    participant.username,
    participant.firstName,
    participant.lastName
  );
}

export function getDisplayNameFromUser(
  user: components["schemas"]["UserResponse"]
) {
  const unknown = "UNK";
  return getDisplayName(
    user.username || unknown,
    user.firstName || unknown,
    user.lastName || unknown
  );
}

function getDisplayName(username: string, firstName: string, lastName: string) {
  return `${username} (${firstName} ${lastName})`;
}
