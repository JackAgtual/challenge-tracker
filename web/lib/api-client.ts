import createClient from "openapi-fetch";
import type { paths } from "@/types/api";
import { auth0 } from "./auht0";

export const client = createClient<paths>({
  baseUrl: process.env.API_BASE_URL || "http://localhost:8080",
});

client.use({
  async onRequest({ request }) {
    const session = await auth0.getSession();
    if (session?.tokenSet.accessToken) {
      request.headers.set(
        "Authorization",
        `Bearer ${session.tokenSet.accessToken}`
      );
    }
    return request;
  },
});
