import CenterContainer from "@/components/CenterContainer";
import { client } from "@/lib/api-client";
import { getValidFirstTimeSession } from "@/lib/auth-utils";
import AccountSetupForm from "./components/AccountSetupForm";

export default async function Page() {
  await getValidFirstTimeSession();

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
    <CenterContainer>
      <AccountSetupForm defaultValues={defaultValues} />
    </CenterContainer>
  );
}
