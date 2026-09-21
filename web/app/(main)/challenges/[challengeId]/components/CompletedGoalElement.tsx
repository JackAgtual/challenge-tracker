"use client";

import { handleGoalUncompletion } from "@/actions/goal-completion";
import { toastGenericError } from "@/lib/toast-utils";
import { CircleCheck } from "lucide-react";

type GoalCompletionIconProps = {
  challengeId: number;
  goalDefinitionId: number;
  goalCompletionId: number;
  challengeInProgress: boolean;
};

export default function GoalCompletionIcon({
  challengeId,
  goalDefinitionId,
  goalCompletionId,
  challengeInProgress,
}: GoalCompletionIconProps) {
  if (!challengeInProgress) {
    return <CircleCheck />;
  }

  async function handleClick() {
    const { success } = await handleGoalUncompletion({
      challengeId,
      goalCompletionId,
      goalDefinitionId,
    });

    if (!success) {
      toastGenericError();
    }
  }

  return <CircleCheck className="cursor-pointer" onClick={handleClick} />;
}
