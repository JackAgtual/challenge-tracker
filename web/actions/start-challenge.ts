"use server";

import { client } from "@/lib/api-client";
import { revalidatePath } from "next/cache";

export async function startChallenge(challengeId: number) {
  const { error } = await client.POST("/challenges/{challengeId}/start", {
    params: { path: { challengeId } },
  });

  if (error) {
    return { success: false as const };
  }

  revalidatePath(`/challenges/${challengeId}`);
  return { success: true };
}
