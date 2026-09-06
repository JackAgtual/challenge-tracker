import { redirect } from "next/navigation";
import { auth0 } from "./auth0";
import { client } from "./api-client";

export async function getValidUser() {
  const session = await auth0.getSession();

  if (!session) {
    redirect("/auth/login");
  }

  const isAccountSetup = await client.GET("/users/me/is-setup");
  if (!isAccountSetup.data?.value) {
    redirect("/account-setup");
  }

  return session;
}
