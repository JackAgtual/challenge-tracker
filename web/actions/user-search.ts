"use server";

import { client } from "@/lib/api-client";

export async function searchUsers(query: string) {
  const { data, error } = await client.GET("/users", {
    params: { query: { query } },
  });

  if (error) {
    return { success: false as const, data: undefined, error };
  }

  return { success: true as const, data, error: undefined };
}
