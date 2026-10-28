"use server";

import { client } from "@/lib/api-client";
import { ActionResponse } from "@/types/action-types";
import { revalidatePath } from "next/cache";

export async function removeParticipant(
  challengeId: number,
  participantId: number
): Promise<ActionResponse> {
  const { error } = await client.DELETE(
    "/challenges/{challengeId}/participants/{participantId}",
    { params: { path: { challengeId, participantId } } }
  );

  if (error) {
    return { success: false, error };
  }

  revalidatePath(`/challenges/${challengeId}`);
  return { success: true };
}
