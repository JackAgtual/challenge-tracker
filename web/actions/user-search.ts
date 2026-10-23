"use server";

import { client } from "@/lib/api-client";
import { ActionResponseWithData } from "@/types/action-types";
import { components } from "@/types/api";

export async function searchUsers(
  query: string
): Promise<
  ActionResponseWithData<components["schemas"]["ValidUserResponse"][]>
> {
  const { data, error } = await client.GET("/users", {
    params: { query: { query } },
  });

  if (error) {
    return { success: false, error };
  }

  return { success: true, data };
}
