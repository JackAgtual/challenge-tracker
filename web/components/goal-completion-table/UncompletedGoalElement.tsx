"use client";

import { handleGoalCompletion } from "@/actions/goal-completion";
import { toastProblemDetail } from "@/lib/toast-utils";
import { Circle } from "lucide-react";

type UncompletedGoalElementProps = {
  challengeId: number;
  goalDefinitionId: number;
  date: string;
  canModifyGoalCompletions: boolean;
};

export function UncompletedGoalElement({
  challengeId,
  goalDefinitionId,
  date,
  canModifyGoalCompletions,
}: UncompletedGoalElementProps) {
  if (!canModifyGoalCompletions) {
    return <Circle />;
  }

  async function handleClick() {
    const { success, error } = await handleGoalCompletion({
      challengeId,
      goalDefinitionId,
      date,
    });

    if (!success) {
      toastProblemDetail(error);
    }
  }

  return <Circle className="cursor-pointer" onClick={handleClick} />;
}
