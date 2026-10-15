"use server";

import { client } from "@/lib/api-client";
import { ActionResponse } from "@/types/action-types";
import { revalidatePath } from "next/cache";

export async function handleGoalCompletion({
  challengeId,
  goalDefinitionId,
  date,
}: {
  challengeId: number;
  goalDefinitionId: number;
  date: string;
}): Promise<ActionResponse> {
  const { error } = await client.POST(
    "/challenges/{challengeId}/goals/{goalDefinitionId}/completions",
    { params: { path: { challengeId, goalDefinitionId } }, body: { date } }
  );
  if (error) {
    return { success: false, error };
  }

  revalidatePath(`/challenges/${challengeId}`);
  return { success: true };
}

export async function handleGoalUncompletion({
  challengeId,
  goalDefinitionId,
  goalCompletionId,
}: {
  challengeId: number;
  goalDefinitionId: number;
  goalCompletionId: number;
}): Promise<ActionResponse> {
  const { error } = await client.DELETE(
    "/challenges/{challengeId}/goals/{goalDefinitionId}/completions/{goalCompletionId}",
    { params: { path: { challengeId, goalCompletionId, goalDefinitionId } } }
  );

  if (error) {
    return { success: false, error };
  }

  revalidatePath(`/challenges/${challengeId}`);
  return { success: true };
}
