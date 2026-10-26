"use client";

import { handleGoalUncompletion } from "@/actions/goal-completion";
import { toastProblemDetail } from "@/lib/toast-utils";
import { CircleCheck } from "lucide-react";

type Props = {
  challengeId: number;
  goalDefinitionId: number;
  goalCompletionId: number;
  canModifyGoalCompletions: boolean;
};

export function CompletedGoalElement({
  challengeId,
  goalDefinitionId,
  goalCompletionId,
  canModifyGoalCompletions,
}: Props) {
  if (!canModifyGoalCompletions) {
    return <CircleCheck />;
  }

  async function handleClick() {
    const { success, error } = await handleGoalUncompletion({
      challengeId,
      goalCompletionId,
      goalDefinitionId,
    });

    if (!success) {
      toastProblemDetail(error);
    }
  }

  return <CircleCheck className="cursor-pointer" onClick={handleClick} />;
}
