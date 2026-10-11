import { client } from "@/lib/api-client";
import ClientButton from "./ClientButton";
import { getValidSession } from "@/lib/auth-utils";

type StartChallengeButtonProps = {
  challengeId: number;
};

export default async function StartChallengeButton({
  challengeId,
}: StartChallengeButtonProps) {
  await getValidSession();
  const startEligibility = await client.GET(
    "/challenges/{challengeId}/start-eligibility",
    {
      params: { path: { challengeId } },
    }
  );

  if (startEligibility.error) {
    throw new Error(
      `Couldn't get challenge start eligibility for challenge ${challengeId}`
    );
  }

  return (
    <ClientButton
      challengeId={challengeId}
      startEligibility={startEligibility.data}
    />
  );
}
