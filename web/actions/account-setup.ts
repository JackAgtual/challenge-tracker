"use server";
import { client } from "@/lib/api-client";
import { ActionResponse } from "@/types/action-types";
import { TSetupAccountFormSchema } from "@/types/form-types";

export async function accountSetup(
  formData: TSetupAccountFormSchema
): Promise<ActionResponse> {
  const { error } = await client.PATCH("/users/me", {
    body: formData,
  });
  if (!error) {
    return {
      success: true,
    };
  }

  return {
    success: false,
    error,
  };
}
