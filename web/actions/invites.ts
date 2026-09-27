"use server";

import { client } from "@/lib/api-client";
import { components } from "@/types/api";
import { revalidatePath } from "next/cache";
import { success } from "zod";

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

export async function inviteUser(
  challengeId: number,
  body: components["schemas"]["SendInviteRequest"]
) {
  const { error } = await client.POST("/challenges/{challengeId}/invites", {
    params: { path: { challengeId } },
    body,
  });

  if (error) {
    return { success: false as const, error };
  }

  revalidatePath(`/challenges/${challengeId}`);
  return { success: true };
}
