"use server";

import { client } from "@/lib/api-client";

export async function removeParticipant(
  challengeId: number,
  participantId: number
) {
  const { error } = await client.DELETE(
    "/challenges/{challengeId}/participants/{participantId}",
    { params: { path: { challengeId, participantId } } }
  );

  if (error) {
    return { success: false as const, error };
  }

  return { success: true };
}
