import { getValidFirstTimeUser } from "@/lib/auth-utils";
import AccountSetupForm from "./components/AccountSetupForm";
import { client } from "@/lib/api-client";

export default async function Page() {
  await getValidFirstTimeUser();

  const { data, error } = await client.GET("/users/me");

  if (error) {
    throw new Error("Failed ot load your data");
  }

  const defaultValues = {
    firstName: data.firstName || "",
    lastName: data.lastName || "",
    username: data.username || "",
  };

  return (
    <>
      <h1>Finish setting up your account</h1>
      <AccountSetupForm defaultValues={defaultValues} />
    </>
  );
}
