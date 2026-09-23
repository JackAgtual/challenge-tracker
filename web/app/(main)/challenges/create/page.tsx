import ChallengeForm from "@/components/ChallengeForm";
import { getValidSession } from "@/lib/auth-utils";

export default async function Page() {
  await getValidSession();

  return (
    <>
      <h1>Create a challenge</h1>
      <ChallengeForm action="CREATE" />
    </>
  );
}
