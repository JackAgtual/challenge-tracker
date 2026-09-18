import ChallengeOverviewCard from "@/components/ChallengeOverviewCard";
import { client } from "@/lib/api-client";
import { getValidSession } from "@/lib/auth-utils";
import GoalCompletionTable from "./components/GoalCompletionTable";
import PendingChallengePage from "./components/pending-challenges/PendingChallengePage";

export default async function Page({
  params,
}: {
  params: Promise<{ challengeId: number }>;
}) {
  await getValidSession();
  const { challengeId } = await params;

  const challengeDetailRes = await client.GET("/challenges/{challengeId}", {
    params: { path: { challengeId } },
  });

  if (challengeDetailRes.error) {
    throw new Error(`Could not find challenge with id=${challengeId}`);
  }

  const { challenge } = challengeDetailRes.data;
  return (
    <>
      <ChallengeOverviewCard challenge={challenge} />
      {challenge.status === "PENDING" ? (
        <PendingChallengePage challengeDetails={challengeDetailRes.data} />
      ) : (
        <GoalCompletionTable
          challengeId={challengeId}
          challengeInProgress={challenge.status === "IN_PROGRESS"}
        />
      )}
    </>
  );
}
