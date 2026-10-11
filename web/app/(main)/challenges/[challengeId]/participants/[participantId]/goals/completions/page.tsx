import GoalCompletionTable from "@/components/goal-completion-table";
import { getValidSession } from "@/lib/auth-utils";

export default async function Page({
  params,
}: {
  params: Promise<{ challengeId: number; participantId: number }>;
}) {
  await getValidSession();

  const { challengeId, participantId } = await params;

  return (
    <GoalCompletionTable
      challengeId={challengeId}
      curParticipantId={participantId}
      canModifyGoalCompletions={false}
    />
  );
}
