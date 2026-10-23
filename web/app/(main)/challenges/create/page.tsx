import CenterContainer from "@/components/CenterContainer";
import ChallengeForm from "@/components/ChallengeForm";
import { getValidSession } from "@/lib/auth-utils";

export default async function Page() {
  await getValidSession();

  return (
    <CenterContainer>
      <ChallengeForm action="CREATE" />
    </CenterContainer>
  );
}
