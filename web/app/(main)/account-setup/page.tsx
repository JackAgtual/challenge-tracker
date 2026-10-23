import CenterContainer from "@/components/CenterContainer";
import { client } from "@/lib/api-client";
import { getValidFirstTimeSession } from "@/lib/auth-utils";
import AccountSetupForm from "./components/AccountSetupForm";

export default async function Page() {
  await getValidFirstTimeSession();

  const [curUser, avatars] = await Promise.all([
    client.GET("/users/me"),
    client.GET("/icons/avatars"),
  ]);

  if (curUser.error) {
    throw new Error("Failed ot load your data");
  }

  if (avatars.error) {
    throw new Error("Failed ot load available avatars");
  }

  const defaultValues = {
    firstName: curUser.data.firstName || "",
    lastName: curUser.data.lastName || "",
    username: curUser.data.username || "",
    avatar: curUser.data.avatarSrc,
  };

  return (
    <CenterContainer>
      <AccountSetupForm
        defaultValues={defaultValues}
        availableAvatars={avatars.data}
      />
    </CenterContainer>
  );
}
