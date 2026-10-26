import { client } from "@/lib/api-client";
import { ClientButton } from "./ClientButton";
import { getValidSession } from "@/lib/auth-utils";

type Props = {
  challengeId: number;
};

export async function StartChallengeButton({ challengeId }: Props) {
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
