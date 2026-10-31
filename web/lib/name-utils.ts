import { components } from "@/types/api";

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

export function getChallengeNameAndDuration(
  challenge: components["schemas"]["ChallengeResponse"]
) {
  return `${challenge.name} - ${challenge.durationDays} ${challenge.durationDays === 1 ? "day" : "days"}`;
}

export function getInitials(user: components["schemas"]["UserResponse"]) {
  const { firstName, lastName } = user;
  const firstInitial = !!firstName ? firstName.charAt(0).toUpperCase() : "";
  const lastInitial = !!lastName ? lastName.charAt(0).toUpperCase() : "";

  return firstInitial || lastInitial ? firstInitial + lastInitial : "?";
}
