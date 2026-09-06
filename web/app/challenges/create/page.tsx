import { getValidSession } from "@/lib/auth-utils";
import CreateChallengeForm from "./components/CreateChallengeForm";

export default async function Page() {
  await getValidSession();

  return (
    <>
      <h1>Create a challenge</h1>
      <CreateChallengeForm />
    </>
  );
}
