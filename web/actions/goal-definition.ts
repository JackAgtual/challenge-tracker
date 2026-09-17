"use server";

import { client } from "@/lib/api-client";
import { TCreateGoalFormSchema } from "@/types/form-types";
import { success } from "zod";

export async function createGoal(
  challengeId: number,
  formData: TCreateGoalFormSchema
) {
  const { error } = await client.POST("/challenges/{challengeId}/goals", {
    params: { path: { challengeId } },
    body: formData,
  });

  if (!error) {
    return { success: true as const };
  }

  return { success: false, error };
}

export async function deleteGoal(
  challengeId: number,
  goalDefinitionId: number
) {
  const { error } = await client.DELETE(
    "/challenges/{challengeId}/goals/{goalDefinitionId}",
    {
      params: { path: { challengeId, goalDefinitionId } },
    }
  );
  if (!error) {
    return { success: true as const };
  }

  return { success: false, error };
}
