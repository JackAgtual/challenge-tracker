import { redirect } from "next/navigation";
import { auth0 } from "./auth0";
import { client } from "./api-client";

// Will check for valid session and account is set up
export async function getValidSession() {
  const session = await getValidFirstTimeSession();

  const isAccountSetup = await client.GET("/users/me/is-setup");
  if (!isAccountSetup.data?.value) {
    redirect("/account-setup");
  }

  return session;
}

// Use when you don't need to check for account setup
export async function getValidFirstTimeSession() {
  const session = await auth0.getSession();

  if (!session) {
    redirect("/auth/login");
  }

  return session;
}
