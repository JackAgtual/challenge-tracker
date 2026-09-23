import { client } from "@/lib/api-client";
import ClientButton from "./ClientButton";

type StartChallengeButtonProps = {
  challengeId: number;
};

export default async function StartChallengeButton({
  challengeId,
}: StartChallengeButtonProps) {
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
