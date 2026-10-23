"use server";

import { client } from "@/lib/api-client";
import { ActionResponse } from "@/types/action-types";
import { revalidatePath } from "next/cache";

export async function startChallenge(
  challengeId: number
): Promise<ActionResponse> {
  const { error } = await client.POST("/challenges/{challengeId}/start", {
    params: { path: { challengeId } },
  });

  if (error) {
    return { success: false, error };
  }

  revalidatePath(`/challenges/${challengeId}`);
  return { success: true };
}
