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
