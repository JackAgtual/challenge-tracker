"use client";

import { deleteGoal } from "@/actions/goal-definition";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuGroup,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import { MoreHorizontalIcon } from "lucide-react";

type GoalDefinitionActionMenuProps = {
  challengeId: number;
  goalDefinitionId: number;
};

export default function GoalDefinitionActionMenu({
  challengeId,
  goalDefinitionId,
}: GoalDefinitionActionMenuProps) {
  async function handleDelete() {
    const res = await deleteGoal(challengeId, goalDefinitionId);

    if (res.success) {
      return;
    }

    // TODO: Handle error
    console.error(res.error);
  }

  return (
    <DropdownMenu>
      <DropdownMenuTrigger
        nativeButton={false}
        render={<MoreHorizontalIcon />}
      />
      <DropdownMenuContent>
        <DropdownMenuGroup>
          <DropdownMenuItem variant="destructive" onClick={handleDelete}>
            Delete
          </DropdownMenuItem>
        </DropdownMenuGroup>
      </DropdownMenuContent>
    </DropdownMenu>
  );
}
