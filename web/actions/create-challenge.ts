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
