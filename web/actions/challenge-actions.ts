"use server";

import { client } from "@/lib/api-client";
import { ActionResponse, ActionResponseWithData } from "@/types/action-types";
import { components } from "@/types/api";
import { TChallengeDataFormSchema } from "@/types/form-types";

export async function createChallenge(
  formData: TChallengeDataFormSchema
): Promise<ActionResponseWithData<components["schemas"]["IdResponse"]>> {
  const { error, data } = await client.POST("/challenges", { body: formData });

  if (!error) {
    return { success: true, data };
  }

  return { success: false, error };
}

export async function modifyChallenge(
  formData: TChallengeDataFormSchema,
  challengeId: number
): Promise<ActionResponse> {
  const { error } = await client.PUT("/challenges/{challengeId}", {
    params: { path: { challengeId } },
    body: formData,
  });

  if (!error) {
    return { success: true };
  }

  return { success: false, error };
}
