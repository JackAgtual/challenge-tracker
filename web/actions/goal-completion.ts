"use server";

import { client } from "@/lib/api-client";
import { revalidatePath } from "next/cache";

export async function handleGoalCompletion({
  challengeId,
  goalDefinitionId,
  date,
}: {
  challengeId: number;
  goalDefinitionId: number;
  date: string;
}) {
  const { error } = await client.POST(
    "/challenges/{challengeId}/goals/{goalDefinitionId}/completions",
    { params: { path: { challengeId, goalDefinitionId } }, body: { date } }
  );
  if (error) {
    return { success: false as const };
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
}) {
  const { error } = await client.DELETE(
    "/challenges/{challengeId}/goals/{goalDefinitionId}/completions/{goalCompletionId}",
    { params: { path: { challengeId, goalCompletionId, goalDefinitionId } } }
  );

  if (error) {
    return { success: false as const };
  }

  revalidatePath(`/challenges/${challengeId}`);
  return { success: true };
}
