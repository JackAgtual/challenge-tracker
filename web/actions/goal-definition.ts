"use server";

import { client } from "@/lib/api-client";
import { ActionResponse } from "@/types/action-types";
import { TCreateGoalFormSchema } from "@/types/form-types";
import { revalidatePath } from "next/cache";

export async function createGoal(
  challengeId: number,
  formData: TCreateGoalFormSchema
): Promise<ActionResponse> {
  const { error } = await client.POST("/challenges/{challengeId}/goals", {
    params: { path: { challengeId } },
    body: formData,
  });

  if (!error) {
    revalidatePath(`/challenges/${challengeId}`);
    return { success: true };
  }

  return { success: false, error };
}

export async function deleteGoal(
  challengeId: number,
  goalDefinitionId: number
): Promise<ActionResponse> {
  const { error } = await client.DELETE(
    "/challenges/{challengeId}/goals/{goalDefinitionId}",
    {
      params: { path: { challengeId, goalDefinitionId } },
    }
  );
  if (!error) {
    revalidatePath(`/challenges/${challengeId}`);
    return { success: true };
  }

  return { success: false, error };
}
