"use server";

import { client } from "@/lib/api-client";
import { ActionResponse } from "@/types/action-types";
import { revalidatePath } from "next/cache";
export async function setReady(
  ready: boolean,
  challengeId: number
): Promise<ActionResponse> {
  const { error } = await client.POST("/challenges/{challengeId}/ready", {
    params: { path: { challengeId } },
    body: { ready },
  });

  if (error) {
    return { success: false, error };
  }

  revalidatePath(`/challenges/${challengeId}`);
  return { success: true };
}
