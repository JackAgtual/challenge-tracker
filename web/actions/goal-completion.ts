"use server";

import { client } from "@/lib/api-client";

export async function handleGoalCompletion({
  challengeId,
  goalDefinitionId,
  date,
}: {
  challengeId: number;
  goalDefinitionId: number;
  date: string;
}) {
  const res = await client.POST(
    "/challenges/{challengeId}/goals/{goalDefinitionId}/completions",
    { params: { path: { challengeId, goalDefinitionId } }, body: { date } }
  );
  // TODO: Add error handling and revalidate
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
  const res = await client.DELETE(
    "/challenges/{challengeId}/goals/{goalDefinitionId}/completions/{goalCompletionId}",
    { params: { path: { challengeId, goalCompletionId, goalDefinitionId } } }
  );
  // TODO: Add error handling and revalidate
}
