"use server";

import { client } from "@/lib/api-client";
import { ActionResponse } from "@/types/action-types";
import { components } from "@/types/api";
import { revalidatePath } from "next/cache";

export async function acceptInvite(inviteId: number): Promise<ActionResponse> {
  const { error } = await client.POST("/invites/{inviteId}/accept", {
    params: { path: { inviteId } },
  });

  if (error) {
    return { success: false, error };
  }
  return { success: true };
}

export async function declineInvite(inviteId: number): Promise<ActionResponse> {
  const { error } = await client.POST("/invites/{inviteId}/decline", {
    params: { path: { inviteId } },
  });

  if (error) {
    return { success: false, error };
  }
  revalidatePath("/invites");
  return { success: true };
}

export async function inviteUser(
  challengeId: number,
  body: components["schemas"]["SendInviteRequest"]
): Promise<ActionResponse> {
  const { error } = await client.POST("/challenges/{challengeId}/invites", {
    params: { path: { challengeId } },
    body,
  });

  if (error) {
    return { success: false, error };
  }

  revalidatePath(`/challenges/${challengeId}`);
  return { success: true };
}
