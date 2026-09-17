import { client } from "@/lib/api-client";
import { getValidSession } from "@/lib/auth-utils";
import GoalCompletionTable from "./components/GoalCompletionTable";
import ParticipantStatus from "./components/ParticipantStatus";
import CreateGoalForm from "./components/pending-challenges/CreateGoalForm";
import GoalDefinitions from "./components/pending-challenges/GoalDefinitions";
import ChallengeOverviewCard from "@/components/ChallengeOverviewCard";

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

  const { challenge, participants } = challengeDetailRes.data;
  return (
    <>
      <ChallengeOverviewCard challenge={challenge} />
      <ParticipantStatus participants={participants} />
      <h2>Your goals</h2>
      {challenge.status === "PENDING" ? (
        <div>
          <GoalDefinitions challengeId={challengeId} />
          <CreateGoalForm challengeId={challengeId} />
        </div>
      ) : (
        <GoalCompletionTable
          challengeId={challengeId}
          challengeInProgress={challenge.status === "IN_PROGRESS"}
        />
      )}
    </>
  );
}
