"use server";

import { client } from "@/lib/api-client";
import { revalidatePath } from "next/cache";

export async function acceptInvite(inviteId: number) {
  const { error } = await client.POST("/invites/{inviteId}/accept", {
    params: { path: { inviteId } },
  });

  if (error) {
    return { success: false as const };
  }
  return { success: true };
}

export async function declineInvite(inviteId: number) {
  const { error } = await client.POST("/invites/{inviteId}/decline", {
    params: { path: { inviteId } },
  });

  if (error) {
    return { success: false as const };
  }
  revalidatePath("/invites");
  return { success: true };
}
