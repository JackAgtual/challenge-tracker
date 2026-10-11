import GoalCompletionTable from "@/components/goal-completion-table";
import { getValidSession } from "@/lib/auth-utils";

export default async function Page({
  params,
}: {
  params: Promise<{ challengeId: string; participantId: string }>;
}) {
  await getValidSession();

  const { challengeId: challengeIdStr, participantId: participantIdStr } =
    await params;
  const challengeId = Number(challengeIdStr);
  const participantId = Number(participantIdStr);

  return (
    <GoalCompletionTable
      challengeId={challengeId}
      curParticipantId={participantId}
      canModifyGoalCompletions={false}
    />
  );
}
