import { Auth0Client } from "@auth0/nextjs-auth0/server";
import { NextResponse } from "next/server";
import { client } from "./api-client";

function applicationUrl(path = "") {
  const appBaseUrl = process.env.APP_BASE_URL;

  if (!appBaseUrl) {
    throw new Error("APP_BASE_URl must be configured");
  }

  const baseUrl = new URL(appBaseUrl);
  baseUrl.pathname = baseUrl.pathname.replace(/\/+$/, "") + "/";

  return new URL(path.replace(/^\/+/, ""), baseUrl);
}

function redirect(path: string | undefined) {
  return NextResponse.redirect(applicationUrl(path));
}

export const auth0 = new Auth0Client({
  signInReturnToPath: "/dashboard",
  authorizationParameters: {
    audience: process.env.AUTH0_AUDIENCE,
  },
  async onCallback(error, context, session) {
    if (error) {
      throw new Error(error.message);
    }

    if (!session) {
      return redirect(context.returnTo);
    }

    // Need to explicitly set authHeaders here because middleware relies on auth.getSession()
    // which hasn't been written yet
    const authHeader = {
      headers: {
        Authorization: `Bearer ${session.tokenSet.accessToken}`,
      },
    };

    await client.POST("/users", {
      body: {
        email: session.user.email || "",
      },
      ...authHeader,
    });

    const userIsSetUp = await client.GET("/users/me/is-setup", authHeader);

    if (!userIsSetUp.data?.value) {
      // Need to finish setting up user account
      return NextResponse.redirect(
        new URL("/account-setup", process.env.APP_BASE_URL)
      );
    }

    return redirect(context.returnTo);
  },
});
