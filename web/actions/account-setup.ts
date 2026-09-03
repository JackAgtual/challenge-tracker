"use server";
import { client } from "@/lib/api-client";
import { TSetupAccountFormSchema } from "@/types/form-types";

export async function accountSetup(formData: TSetupAccountFormSchema) {
  const { error } = await client.PATCH("/users/me", {
    body: formData,
  });
  if (!error) {
    return {
      success: true as const,
    };
  }

  return {
    success: false,
    error,
  };
  const x = 1;
}
