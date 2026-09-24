"use server";

import { client } from "@/lib/api-client";
import { TChallengeDataFormSchema } from "@/types/form-types";

export async function createChallenge(formData: TChallengeDataFormSchema) {
  const { error, data } = await client.POST("/challenges", { body: formData });

  if (!error) {
    return { success: true as const, data };
  }

  return { success: false, error };
}

export async function modifyChallenge(
  formData: TChallengeDataFormSchema,
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
