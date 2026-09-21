"use server";

import { client } from "@/lib/api-client";
import { revalidatePath } from "next/cache";
export async function setReady(ready: boolean, challengeId: number) {
  const res = await client.POST("/challenges/{challengeId}/ready", {
    params: { path: { challengeId } },
    body: { ready },
  });

  if (res.error) {
    return { success: false as const };
  }

  revalidatePath(`/challenges/${challengeId}`);
  return { success: true };
}
