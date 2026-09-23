"use server";

import { client } from "@/lib/api-client";
import { TCreateChallengeFormSchema } from "@/types/form-types";

export async function createChallenge(formData: TCreateChallengeFormSchema) {
  const { error, data } = await client.POST("/challenges", { body: formData });

  if (!error) {
    return { success: true as const, data };
  }

  return { success: false, error };
}

export async function modifyChallenge(
  formData: TCreateChallengeFormSchema,
  challengeId: number
) {
  const { error } = await client.PUT("/challenges/{challengeId}", {
    params: { path: { challengeId } },
    body: formData,
  });

  if (!error) {
    return { success: true as const };
  }

  return { success: false, error };
}
